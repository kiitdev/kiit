package kiit.migrations

import kiit.common.data.IDb
import kiit.result.Outcome
import kiit.result.Outcomes

data class SqlMigrationStep(
    override val type: MigrationType,
    val sql: String
) : MigrationStep {

    override fun run(db: IDb): Outcome<String> {
        return when (type) {
            MigrationType.Custom -> { db.execute(sql); Outcomes.success(sql) }
            else -> Outcomes.rejected("Unexpected migration type")
        }
    }
}
