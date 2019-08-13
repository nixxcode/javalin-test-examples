import static org.assertj.core.api.Assertions.*;

import io.javalin.Javalin;
import io.javalin.core.util.Header;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.junit.*;

import java.io.Serializable;

public class TestExamples {
    static int port = 7000;
    static String origin = "http://localhost:" + port;
    static Javalin app;

    @BeforeClass
    public static void setup() {
        app = Javalin.create().start(port);
        app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/json", ctx -> ctx.json(new SerializeableObject()));
    }

    @AfterClass
    public static void finish() {
        app.stop();
    }

    @Test
    public void helloWorld() throws UnirestException {
        String response = Unirest.get(origin + "/").header(Header.ACCEPT_ENCODING, "null").asString().getBody();
        assertThat(response).isEqualTo("Hello World");
        assertThat(response).isNotEqualTo("heLLo woRLd!");
    }

    @Test
    public void helloWorldJSON() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.get(origin + "/json").header(Header.ACCEPT, "application/json").asJson();
        String name = jsonResponse.getBody().getObject().getString("name");
        String value = jsonResponse.getBody().getObject().getString("value");
        assertThat(name).isEqualTo("greeting");
        assertThat(value).isEqualTo("Hello World");
    }
}

class SerializeableObject implements Serializable {
    public String name = "greeting";
    public String value = "Hello World";
}
