# Benchmarks

## `EntityUtils.isAboveWater()` ~22.8% Faster

### Optimizations
- Cache `WorldChunk` object
- Mutate y coordinate instead of `BlockPos.Mutable` object
- Use BlockPos-less `WorldChunk.getBlockState()` implementation
- Check for `BlockState` equality instead of `FluidState` equality (`Blocks.WATER` instead of both `Fluids.WATER` & `Fluids.FLOWING_WATER`)

### Default Meteor implementation results
Standard Deviation: 4496.0 ns  
Mean: 12402 ns  
Median: 11700 ns  
90th Percentile: 15300 ns  
95th Percentile: 18700 ns  
99th Percentile: 26700 ns  

### Electron implementation results
Standard Deviation: 3016.7 ns  
Mean: 9580 ns  
Median: 8600 ns  
90th Percentile: 14500 ns  
95th Percentile: 15300 ns  
99th Percentile: 16600 ns  

## `ChunkBlockIterator` ~13.9% Faster

### Optimizations
- Cache `WorldChunk` object
- Mutate coordinates instead of `BlockPos.Mutable` object
- Use BlockPos-less `WorldChunk.getBlockState()` implementation

### Default Meteor implementation results
Standard Deviation: 1190.7 μs  
Mean: 44697 μs  
Median: 40253 μs  
90th Percentile: 69443 μs  
95th Percentile: 72395 μs  
99th Percentile: 79192 μs

### Electron implementation results
Standard Deviation: 1628.8 μs  
Mean: 38475 μs  
Median: 33948 μs  
90th Percentile: 56249 μs  
95th Percentile: 80313 μs  
99th Percentile: 88086 μs  
