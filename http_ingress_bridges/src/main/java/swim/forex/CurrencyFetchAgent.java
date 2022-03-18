// Copyright 2015-2022 SWIM.AI inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package swim.forex;

import java.io.IOException;
import swim.api.agent.AbstractAgent;
import swim.concurrent.AbstractTask;
import swim.concurrent.TaskRef;
import swim.concurrent.TimerRef;

public class CurrencyFetchAgent extends AbstractAgent {

  private static final long POLL_DELAY_MS = 20L * 1000;

  private TimerRef timer;
  private TaskRef poll;

  private void initPoll() {
    this.poll = asyncStage().task(new AbstractTask() {

      @Override
      public void runTask() {
        try {
          FreeForexApi.relayExchangeRates(agentContext());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public boolean taskWillBlock() {
        return true;
      }
    });
  }

  private void scheduleTimer() {
    if (this.timer != null) {
      return;
    }
    this.timer = setTimer(0L, () -> {
      if (this.poll == null) {
        initPoll();
      }
      this.poll.cue();
      this.timer.reschedule(POLL_DELAY_MS);
    });
  }

  @Override
  public void didStart() {
    System.out.println(nodeUri() + ": didStart");
    scheduleTimer();
  }

}
