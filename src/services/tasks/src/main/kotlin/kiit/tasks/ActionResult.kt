package kiit.tasks

import kiit.result.Outcome

/**
 * Contains the full context of processing a task.
 */
data class ActionResult(val action: Action,
                        val worker: Worker,
                        val task: Task,
                        val result: Outcome<*>,
                        val stats: Stats)
