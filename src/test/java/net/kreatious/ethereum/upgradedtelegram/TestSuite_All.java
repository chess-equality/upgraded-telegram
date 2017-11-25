package net.kreatious.ethereum.upgradedtelegram;

import net.kreatious.ethereum.upgradedtelegram.erc20.TestSuite_Erc20;
import net.kreatious.ethereum.upgradedtelegram.extensions.TestSuite_Extensions;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all the tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestSuite_Erc20.class,
    TestSuite_Extensions.class
})
public class TestSuite_All {
}
