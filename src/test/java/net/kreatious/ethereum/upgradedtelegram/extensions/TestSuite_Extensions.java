package net.kreatious.ethereum.upgradedtelegram.extensions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs the PositiveTests_Extensions and NegativeTests_Extensions tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    PositiveTests_Extensions.class,
    NegativeTests_Extensions.class
})
public class TestSuite_Extensions {
}
