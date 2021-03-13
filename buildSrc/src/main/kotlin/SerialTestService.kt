import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters


/**
 * 串行执行测试，gradle的test任务默认是并行执行的
 * @author afezeria
 */
class SerialTestService : BuildService<SerialTestService.Params> {
    override fun getParameters(): Params = Params()
    open class Params : BuildServiceParameters
}

class SerialTestPlugin : Plugin<Project?> {
    override fun apply(project: Project) {
        val serviceProvider: Provider<SerialTestService> =
            project.gradle.sharedServices.registerIfAbsent(
                "serialtest", SerialTestService::class.java
            ) { maxParallelUsages.set(1) }
        for (task in project.getTasksByName("test", true)) {
            task.usesService(serviceProvider)
        }
    }
}