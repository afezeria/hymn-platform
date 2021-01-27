package github.afezeria.hymn.oss

import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.constant.ClientType
import github.afezeria.hymn.common.platform.ConfigService
import github.afezeria.hymn.common.platform.PermService
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.platform.SessionService
import github.afezeria.hymn.common.util.randomUUIDStr
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

/**
 * @author afezeria
 */
@TestConfiguration
class OssTestConfiguration {
    @Bean
    fun configService(): ConfigService {
        val mock = mockk<ConfigService>()
        every { mock.getAsString("oss") } returns null
        return mock
    }

    @Bean
    fun sessionService(): SessionService {
        val mock = mockk<SessionService>()
        every { mock.getSession() } returns Session(
            id = randomUUIDStr(),
            accountType = AccountType.ADMIN,
            accountId = "911c60ea5d62420794d86eeecfddce7c",
            accountName = "管理员",
            clientType = ClientType.BROWSER,
            roleId = "301c35c23be449abb5bdf6c80b6878af",
            roleName = "管理员",
            orgId = "b18245e9d690461190172b6cb90c46ac",
            orgName = "根组织"
        )
        return mock
    }

    @Bean
    fun permService(): PermService {
        val mock = mockk<PermService>()
        every { mock.hasFieldPerm(any(), any()) } returns true
        every { mock.hasFunctionPerm(any(), any()) } returns true
        every { mock.hasDataPerm(any(), any()) } returns true
        every { mock.hasObjectPerm(any()) } returns true
        return mock
    }
}