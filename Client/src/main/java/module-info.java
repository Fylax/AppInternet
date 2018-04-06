module Client {
    requires jdk.incubator.httpclient;
    requires jackson.databind;
    requires jackson.core;

    exports it.polito.client;
}