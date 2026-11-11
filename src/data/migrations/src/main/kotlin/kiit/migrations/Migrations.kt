package kiit.migrations

import kiit.common.data.IDb
import kiit.common.log.LogSupport
import kiit.common.log.Logger
import kiit.result.Outcome
import kiit.result.Outcomes
import kiit.result.Tries

data class SimpleMigration(
    override val desc: String,
    override val start: Int,
    override val end: Int,
    override val steps: List<MigrationStep>,
    override val logger: Logger?
) : Migration, LogSupport {

    override fun migrate(db: IDb): Outcome<Int> {
        info("Migration starting for : $id")
        val results = steps.map { Tries.attempt { it.run(db) } }
        val success = results.all { it.success }
        val message = if (success) "" else (results.first { !it.success } as kiit.result.Failure).error.message.orEmpty()
        return when (success) {
            true -> {
                info("Migration success : $id. \n $message")
                Outcomes.success(steps.count())
            }
            false -> {
                info("Migration failure : $id. \n $message")
                Outcomes.rejected(message)
            }
        }
    }
}
