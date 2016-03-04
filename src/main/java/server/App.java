package server;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        }

        after((request, response) -> {
            response.header("Server", "Hosted-Bazel-Deps");
            response.header("X-Credit-Where-Credit-Is-Due", "https://github.com/pgr0ss/bazel-deps");
        });

        get("/", (req, res) -> new ModelAndView(new IndexView(null), "index.hbs"), new HandlebarsTemplateEngine("/server/templates"));

        get("/:artifact", (req, res) -> {
            IndexView idx = new IndexView(req.params("artifact"));
            return new ModelAndView(idx, "index.hbs");
        }, new HandlebarsTemplateEngine("/server/templates"));

        post("/:artifact", (req, res) -> {
            IndexView idx = new IndexView(req.params("artifact"));
            return idx.getMavenDependencies();
        });
    }
}
