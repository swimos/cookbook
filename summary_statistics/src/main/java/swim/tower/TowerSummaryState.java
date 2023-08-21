package swim.tower;

import swim.structure.Record;
import swim.structure.Value;

class TowerSummaryState {

  private double min = Double.MAX_VALUE;
  private double max = -Double.MIN_VALUE;
  private int count = 0;
  private double mean = 0.0;
  private double agg = 0.0;

  public void addValue(double d) {
    this.min = Math.min(this.min, d);
    this.max = Math.max(this.max, d);
    // Welford's algorithm for incremental variance computation
    // (and mean, as a side effect)
    this.count += 1;
    final double delta = d - this.mean;
    this.mean += delta / this.count;
    this.agg += delta * (d - this.mean);
  }

  public Value getSummary() {
    if (this.count == 0) {
      return Value.extant();
    }
    return Record.create(5)
        .slot("count", this.count)
        .slot("min", this.min)
        .slot("max", this.max)
        .slot("avg", this.mean)
        .slot("variance", this.agg / this.count);
    // Note: agg and count trivially transform into more than just variance:
    // - agg / count = variance
    // - sqrt(agg/count) = stdev
    // - agg / (count - 1) = sample variance
    // - sqrt(agg / (count - 1)) = sample stdev
  }

}
