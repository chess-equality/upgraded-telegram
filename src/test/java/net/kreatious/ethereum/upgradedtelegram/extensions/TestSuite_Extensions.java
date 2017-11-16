package net.kreatious.ethereum.upgradedtelegram.extensions;

import net.kreatious.ethereum.upgradedtelegram.extensions.scenario.PuchaseTest;
import net.kreatious.ethereum.upgradedtelegram.extensions.scenario.SellTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs the tests on the additional functions
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    PuchaseTest.class,
    SellTest.class
})
public class TestSuite_Extensions {
}
