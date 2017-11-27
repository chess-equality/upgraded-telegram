package net.kreatious.ethereum.upgradedtelegram.extensions;

import net.kreatious.ethereum.upgradedtelegram.extensions.scenario.DepositTest;
import net.kreatious.ethereum.upgradedtelegram.extensions.scenario.PauseTest;
import net.kreatious.ethereum.upgradedtelegram.extensions.scenario.PurchaseAndSellTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs the tests on the additional functions
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    PurchaseAndSellTest.class,
    PauseTest.class,
    DepositTest.class
})
public class TestSuite_Extensions {
}
