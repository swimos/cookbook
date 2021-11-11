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
               logRequest(true, envelope.nodeUri().toString(), envelope.laneUri().toString(), token);
                return allow();
            }

            //Admin tokens must be used for 'adminInfo' lanes or '/control' agents
            if ("adminInfo".equals(envelope.laneUri().toString()) ||
                    envelope.nodeUri().toString().startsWith("/control")) {
                logRequest(false, envelope.nodeUri().toString(), envelope.laneUri().toString(), token);
                return forbid();
            }

            //Users can access any remaining lanes
            if (USER_TOKEN.equals(token)) {
                logRequest(true, envelope.nodeUri().toString(), envelope.laneUri().toString(), token);
                return allow();
            }
            logRequest(false, envelope.nodeUri().toString(), envelope.laneUri().toString(), token);
        }
        return forbid();
    }

    private static void logRequest(final boolean accepted, final String nodeUri, final String laneUri, final String token) {
        System.out.println("policy: " + (accepted ? "Accepted " : "Rejected ") + "request to " + nodeUri + "/" + laneUri + " with token " + token);
    }
}

