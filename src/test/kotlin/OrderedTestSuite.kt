import org.junit.runner.RunWith
import org.junit.runners.Suite
import tests.BackupTest
import tests.CreateWithXmlElementTest
import tests.IntegrationTest
import tests.PointTest
import tests.StartupTest
import tests.TagRegistryTest
import tests.WallButtonTest
import tests.WriteSvgButtonTest
import tests.XmlTest
import tests.entities.LuminaireTest
import tests.entities.PipeTest
import tests.entities.WallTest

//https://github.com/junit-team/junit4/wiki/aggregating-tests-in-suites

@RunWith(Suite::class)
@Suite.SuiteClasses(
//  XmlTest::class,
//  TagRegistryTest::class,
  StartupTest::class,
//  WallButtonTest::class,
//  WriteSvgButtonTest::class,
//  CreateWithXmlElementTest::class,
//  WallTest::class,
//  PointTest::class,
//  LuminaireTest::class,
//  BackupTest::class,
//  IntegrationTest::class,
  PipeTest::class,
)
class OrderedTestSuite {
}
