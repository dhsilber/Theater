import com.mobiletheatertech.plot.tests.XmlTest
import failing.*
import org.junit.runner.RunWith
import org.junit.runners.Suite
import tests.LuminaireTest
import tests.BackupTest
import tests.PointTest
import tests.WallTest

@RunWith(Suite::class)
@Suite.SuiteClasses(
//  TagRegistryTest::class,
//  StartupTest::class,
//  WallButtonTest::class,
//  WriteSvgButtonTest::class,
//  CreateWithElementTest::class,
//  IntegrationTest::class,
//  WallTest::class,
//  PointTest::class,
  LuminaireTest::class,
  XmlTest::class,
  BackupTest::class,
)
class OrderedTestSuite {
}
