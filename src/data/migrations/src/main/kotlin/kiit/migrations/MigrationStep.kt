package kiit.migrations

import kiit.common.data.IDb
import kiit.result.Outcome

interface MigrationStep {
    val type: MigrationType

    fun run(db: IDb): Outcome<String>
}
