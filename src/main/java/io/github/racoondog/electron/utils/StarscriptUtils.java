package io.github.racoondog.electron.utils;

import io.github.racoondog.electron.ElectronSystem;
import io.github.racoondog.electron.mixin.mixininterface.IStarscript;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.Starscript;
import meteordevelopment.starscript.compiler.Expr;
import meteordevelopment.starscript.compiler.Token;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Things I would do to Starscript to improve code performance
 *
 * Replace Token enum with byte identifiers:
 * - Doesn't mess with equality operators (==, !=)
 * - Enables inequality operators (>, <=, etc.)
 *
 * Add Type enum for Exprs similar to Value.type:
 * - Would replace all Expr.getClass() method calls
 * - Would replace some instanceof operators
 */
@Environment(EnvType.CLIENT)
public final class StarscriptUtils {
    /**
     * Runs the script and returns a raw string. Throws {@link meteordevelopment.starscript.utils.StarscriptError} if a runtime error occurs.
     *
     * @author Crosby
     * @since 0.2.4
     */
    public static String getRawString(Starscript ss, Script script) {
        return getRawString(ss, script, new StringBuilder());
    }

    /**
     * Runs the script and fills the provided {@link StringBuilder} and returns a raw string. Throws {@link meteordevelopment.starscript.utils.StarscriptError} if a runtime error occurs.
     *
     * @author Crosby
     * @since 0.2.4
     */
    public static String getRawString(Starscript ss, Script script, StringBuilder sb) {
        return ((IStarscript) ss).rawRun(script, sb);
    }

    private static Expr _null(Expr base) {
        return ElectronSystem.get().nullOnError.get() ? new Expr.Null(base.start, base.end) : base;
    }

    public static boolean ignoreSections() {
        return ElectronSystem.get().starscript.get() && ElectronSystem.get().ignoreSections.get();
    }

    public static boolean isBooleanOperation(Token op) {
        return op == Token.EqualEqual || op == Token.BangEqual || op == Token.Greater || op == Token.GreaterEqual || op == Token.Less || op == Token.LessEqual;
    }

    public static boolean equals(Expr left, Expr right) {
        if (left.getClass() != right.getClass()) return false;
        else if (left instanceof Expr.Null) return true;
        else if (left instanceof Expr.Bool boolExpr) return boolExpr.bool == ((Expr.Bool) right).bool;
        else if (left instanceof Expr.Number boolExpr) return boolExpr.number == ((Expr.Number) right).number;
        else if (left instanceof Expr.String boolExpr) return boolExpr.string.equals(((Expr.String) right).string);
        else return false;
    }

    //Assumes both Exprs are of the same class
    public static boolean _equals(Expr left, Expr right) {
        if (left instanceof Expr.Null) return true;
        else if (left instanceof Expr.Bool boolExpr) return boolExpr.bool == ((Expr.Bool) right).bool;
        else if (left instanceof Expr.Number boolExpr) return boolExpr.number == ((Expr.Number) right).number;
        else if (left instanceof Expr.String boolExpr) return boolExpr.string.equals(((Expr.String) right).string);
        else return false;
    }

    public static boolean isSolveable(Expr expr) {
        if (expr instanceof Expr.Variable || expr instanceof Expr.Get) return false;
        else if (expr instanceof Expr.Binary binaryExpr) return isSolveable(binaryExpr.right) && isSolveable(binaryExpr.left);
        else if (expr instanceof Expr.Unary unaryExpr) return isSolveable(unaryExpr.right);
        else if (expr instanceof Expr.Logical logicalExpr) return isSolveable(logicalExpr.right) && isSolveable(logicalExpr.left);
        else if (expr instanceof Expr.Conditional conditionalExpr) return isSolveable(conditionalExpr.condition);
        else if (expr instanceof Expr.Section sectionExpr) return isSolveable(sectionExpr.expr);
        else if (expr instanceof Expr.Group groupExpr) return isSolveable(groupExpr.expr);
        else if (expr instanceof Expr.Block blockExpr) return isSolveable(blockExpr.expr);
        else if (expr instanceof Expr.Call callExpr) return isCallSolveable(callExpr);
        else return true;
    }

    public static Expr solve(Expr expr) {
        if (expr instanceof Expr.Binary binaryExpr) return solveBinary(binaryExpr);
        else if (expr instanceof Expr.Unary unaryExpr) return solveUnary(unaryExpr);
        else if (expr instanceof Expr.Logical logicalExpr) return solveLogical(logicalExpr);
        else if (expr instanceof Expr.Conditional conditionalExpr) return solveConditional(conditionalExpr);
        else if (expr instanceof Expr.Section sectionExpr) return solveSection(sectionExpr);
        else if (expr instanceof Expr.Group groupExpr) return solveGroup(groupExpr);
        else if (expr instanceof Expr.Block blockExpr) return solveBlock(blockExpr);
        else if (expr instanceof Expr.Call callExpr) return solveCall(callExpr);
        else return expr;
    }

    public static Expr solveBinary(Expr.Binary expr) {
        if (isSolveable(expr.left) && isSolveable(expr.right)) {
            Expr left = solve(expr.left);
            Expr right = solve(expr.right);

            if (left.getClass() != right.getClass()) return _null(expr); //Incompatible types

            if (ElectronSystem.get().nullPropagation.get() && (left instanceof Expr.Null || right instanceof Expr.Null)) return _null(expr); //Propagate null

            if (expr.op == Token.EqualEqual) {
                return new Expr.Bool(expr.start, expr.end, _equals(left, right));
            } else if (expr.op == Token.BangEqual) {
                return new Expr.Bool(expr.start, expr.end, !_equals(left, right));
            } else if (expr.op == Token.Plus && left instanceof Expr.String leftString) {
                return new Expr.String(expr.start, expr.end, leftString.string + ((Expr.String) right).string);
            } else if (left instanceof Expr.Number leftNumber && right instanceof Expr.Number rightNumber) {
                switch (expr.op) {
                    case Plus:         return new Expr.Number(expr.start, expr.end, rightNumber.number + leftNumber.number);
                    case Minus:        return new Expr.Number(expr.start, expr.end, rightNumber.number - leftNumber.number);
                    case Star:         return new Expr.Number(expr.start, expr.end, rightNumber.number * leftNumber.number);
                    case Slash:        return new Expr.Number(expr.start, expr.end, rightNumber.number / leftNumber.number);
                    case Percentage:   return new Expr.Number(expr.start, expr.end, rightNumber.number % leftNumber.number);
                    case UpArrow:      return new Expr.Number(expr.start, expr.end, Math.pow(rightNumber.number, leftNumber.number));

                    case Greater:      return new Expr.Bool(expr.start, expr.end, leftNumber.number > rightNumber.number);
                    case GreaterEqual: return new Expr.Bool(expr.start, expr.end, leftNumber.number >= rightNumber.number);
                    case Less:         return new Expr.Bool(expr.start, expr.end, leftNumber.number < rightNumber.number);
                    case LessEqual:    return new Expr.Bool(expr.start, expr.end, leftNumber.number <= rightNumber.number);
                }
            }
        } else if (isSolveable(expr.left) && solve(expr.left) instanceof Expr.Number leftNumber) {
            if (expr.op == Token.Star) {
                if (leftNumber.number == 1) return expr.right; // 1 * x = x
                else if (leftNumber.number == 0) return new Expr.Number(expr.start, expr.end, 0.0D); // 0 * x = 0
            } else if (expr.op == Token.Slash && leftNumber.number == 0) return new Expr.Number(expr.start, expr.end, 0.0D); // 0 / x = 0 (Does not account for division by 0)
            else if (expr.op == Token.UpArrow && (leftNumber.number == 0 || leftNumber.number == 1)) return new Expr.Number(expr.start, expr.end, leftNumber.number); // (0|1) ^ x = (0|1)
            else if (expr.op == Token.Plus && leftNumber.number == 0) return expr.right; // 0 + x = x
        } else if (isSolveable(expr.right) && solve(expr.right) instanceof Expr.Number rightNumber) {
            if (expr.op == Token.Star) {
                if (rightNumber.number == 1) return expr.left; // x * 1 = x
                else if (rightNumber.number == 0) return new Expr.Number(expr.start, expr.end, 0.0D); // x * 0 = 0
            } else if (expr.op == Token.Slash) {
                if (rightNumber.number == 1) return expr.left; // x / 1 = x
                else if (rightNumber.number == 0) return _null(expr); //Division by 0
            } else if (expr.op == Token.UpArrow) {
                if (rightNumber.number == 1) return expr.right; // x ^ 1 = x
                else if (rightNumber.number == 0) return new Expr.Number(expr.start, expr.end, 1.0D); // x ^ 0 = 1
            } else if ((expr.op == Token.Plus || expr.op == Token.Minus) && rightNumber.number == 0) return expr.left; // x (+|-) 0 = x
        }

        if (expr.left instanceof Expr.Unary leftUnary && leftUnary.op == Token.Minus && expr.right instanceof Expr.Unary rightUnary && rightUnary.op == Token.Minus) {
            if (expr.op == Token.Star || expr.op == Token.Slash) return new Expr.Binary(expr.start, expr.end, leftUnary.right, expr.op, rightUnary.right); // -x (*|/) -y = x (*|/) y
        }

        return expr;
    }

    public static Expr solveUnary(Expr.Unary expr) {
        if (isSolveable(expr.right)) {
            Expr nestedExpr = solve(expr.right);

            if (nestedExpr instanceof Expr.Bool boolExpr && expr.op == Token.Bang) return new Expr.Bool(expr.start, expr.end, !boolExpr.bool); // !boolean
            else if (nestedExpr instanceof Expr.Number numberExpr && expr.op == Token.Minus) return new Expr.Number(expr.start, expr.end, -numberExpr.number); // -number
            else return _null(expr); //Invalid operation
        }
        return expr;
    }

    public static Expr solveLogical(Expr.Logical expr) {
        boolean leftSolveable = isSolveable(expr.left);
        boolean rightSolveable = isSolveable(expr.right);

        if (leftSolveable && rightSolveable) {
            Expr left = solve(expr.left);
            Expr right = solve(expr.right);
            if (!(left instanceof Expr.Bool) || !(right instanceof Expr.Bool)) return _null(expr); //Not a boolean

            //Constant folding
            if (expr.op == Token.And) return new Expr.Bool(expr.start, expr.end, ((Expr.Bool) left).bool && ((Expr.Bool) right).bool);
            else return new Expr.Bool(expr.start, expr.end, ((Expr.Bool) left).bool || ((Expr.Bool) right).bool);
        } else if (leftSolveable || rightSolveable) {
            Expr solved = leftSolveable ? solve(expr.left) : solve(expr.right);
            Expr unknown = leftSolveable ? expr.right : expr.left;

            if (solved instanceof Expr.Bool boolExpr) {
                if (expr.op == Token.And) {
                    if (boolExpr.bool) return unknown; // true and unknown = unknown
                    else return new Expr.Bool(expr.start, expr.end, false); // false and unknown = false
                } else if (expr.op == Token.Or) {
                    if (boolExpr.bool) return new Expr.Bool(expr.start, expr.end, true); // true or unknown = true
                    else return unknown; // false or unknown = unknown
                }
            } else return _null(expr); //Not a boolean
        }
        return expr; //Both values unknown
    }

    public static Expr solveConditional(Expr.Conditional expr) {
        if (isSolveable(expr.condition)) {
            Expr condition = solve(expr.condition);

            if (condition instanceof Expr.Bool boolExpr) {
                Expr nestedExpr = boolExpr.bool ? expr.trueExpr : expr.falseExpr; //Find path
                return isSolveable(nestedExpr) ? solve(nestedExpr) : nestedExpr; //Try solve path
            } else return _null(expr); //Not a boolean
        }
        return expr;
    }

    public static Expr solveSection(Expr.Section expr) {
        if (isSolveable(expr.expr)) {
            Expr solved = solve(expr.expr);
            if (ElectronSystem.get().ignoreSections.get()) return solved;
            else return new Expr.Section(expr.start, expr.end, expr.index, solved);
        }
        else return expr;
    }

    public static Expr solveGroup(Expr.Group expr) {
        if (isSolveable(expr.expr)) return solve(expr.expr);
        else return expr;
    }

    public static Expr solveBlock(Expr.Block expr) {
        if (isSolveable(expr.expr)) return solve(expr.expr);
        else return expr;
    }

    public static boolean isCallSolveable(Expr.Call expr) {
        if (!ElectronSystem.get().nullPropagation.get()) return false;
        for (var arg : expr.args) {
            if (arg instanceof Expr.Null) return true;
        }
        return false;
    }

    public static Expr solveCall(Expr.Call expr) {
        if (isCallSolveable(expr)) return _null(expr);
        else return expr;
    }
}
