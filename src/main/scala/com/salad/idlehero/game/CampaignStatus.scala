package com.salad.idlehero.game

import com.salad.idlehero.model.Campaign

class CampaignStatus(var currentCampaign: Campaign = null,
                     var currentStageIndex: Int = 0,
                     var stageEnemiesDefeated: Int = 0)