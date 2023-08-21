package swim.tower;

import swim.structure.Record;
import swim.structure.Value;

class TowerSummaryState {

  private double min = Double.MAX_VALUE;
  private double max = -Double.MIN_VALUE;
  private int count = 0;
  private double mean = 0.0;
  private double agg = 0.0;
  private int failures = 0;

  public void addValue(double d, int f) {
    this.min = Math.min(this.min, d);
    this.max = Math.max(this.max, d);
    this.count += 1;
    final double delta = d - this.mean;
    this.mean += delta / this.count;
    this.agg += delta * (d - this.mean);
    this.failures += f;
  }

  public Value getSummary() {
    if (this.count == 0) {
      return Value.extant();
    }
    return Record.create(6)
        .slot("count", this.count)
        .slot("min", this.min)
        .slot("max", this.max)
        .slot("avg", this.mean)
        .slot("variance", this.agg / this.count)
        .slot("failures", this.failures);
  }

}
