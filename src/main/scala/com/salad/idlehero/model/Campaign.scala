package com.salad.idlehero.model

case class Campaign(name: String,
                    enemyDistribution: Map[EnemyClass, Double],
                    elementDistribution: Map[Element, Double],
                    stages: Seq[Stage])
