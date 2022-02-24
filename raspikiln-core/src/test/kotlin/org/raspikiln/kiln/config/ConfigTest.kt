package org.raspikiln.kiln.config

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.raspikiln.test.TestResource
import org.raspikiln.test.YamlMapper

class ConfigTest : FunSpec({
    test("able to load config") {
        val simpleConfig = readConfig("kiln-config-simple")
        simpleConfig.kiln should {
            it.provider shouldBe "rpi"
            it.sensors.shouldHaveSize(1)
            it.switches.shouldHaveSize(1)
        }
    }
})

private fun readConfig(name: String): KilnConfigDefinition =
    YamlMapper.mapper().readValue(TestResource.config(name))