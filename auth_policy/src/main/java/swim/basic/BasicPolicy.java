package swim.basic;

import swim.api.auth.Identity;
import swim.api.policy.AbstractPolicy;
import swim.api.policy.PolicyDirective;
import swim.warp.Envelope;

public class BasicPolicy extends AbstractPolicy {

    private static final String ADMIN_TOKEN = "abc123";
    private static final String USER_TOKEN = "abc";

    @Override
    protected <T> PolicyDirective<T> authorize(Envelope envelope, Identity identity) {
        if (identity != null) {
            final String token = identity.requestUri().query().get("token");
            //Always authorize admins
            if (ADMIN_TOKEN.equals(token)) {
                System.out.println("Accepting admin token: " + token);
                return allow();
            }
            //Only admins should be authorized for this lane
            if ("adminInfo".equals(envelope.laneUri().toString())) {
                System.out.println("Rejecting request to admin lane without admin token");
                return forbid();
            }
            //Users can access any remaining lanes
            if (USER_TOKEN.equals(token)) {
                System.out.println("Accepting user token: " + token);
                return allow();
            }
            System.out.println("Rejecting token: " + token);
        }
        return forbid();
    }
}

