package net.kreatious.ethereum.upgradedtelegram.erc20;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs the PositiveTests_Erc20 and NegativeTests_Erc20 tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    PositiveTests_Erc20.class,
    NegativeTests_Erc20.class
})
public class TestSuite_Erc20 {
}
