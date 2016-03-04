package server;

import braintree.BazelDeps;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.kohsuke.args4j.CmdLineException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static spark.Spark.halt;

public class IndexView {
    private final String mavenDependencies;
    private final String query;

    public IndexView(String artifact) {
        this.mavenDependencies = doMavenDance(artifact);
        this.query = artifact;
    }

    public String getQuery() {
        return query;
    }

    public String getMavenDependencies() {
        return mavenDependencies;
    }

    private String doMavenDance(String artifact) {
        if (artifact == null) {
            return "";
        }

        PrintStream originalSystemOut = System.out;
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        System.setOut(new PrintStream(s));
        try {
            BazelDeps.main(new String[]{artifact});
            System.err.println("Done fetching dependencies from maven!");

            return s.toString();
        } catch (DependencyCollectionException | CmdLineException e) {
            halt(400, e.getLocalizedMessage());
            return "";
        } finally {
            System.setOut(originalSystemOut);
        }
    }
}
