module extra.module {
    requires org.junit.jupiter.api;
    requires org.junit.platform.engine;
    requires org.junit.jupiter.engine;

    requires com.example.application;
    requires com.example.tool;
    requires ice.cream;

    exports extra.module;

    opens extra.module to org.junit.platform.commons, org.junit.jupiter.engine;
}
