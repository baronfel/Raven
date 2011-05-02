package raven.goals;

import raven.game.RavenBot;
import raven.game.RavenObject;
import raven.math.Vector2D;
import raven.ui.GameCanvas;

public class GetWeaponGoal_Evaluator extends Goal_Evaluator {
	private RavenObject weaponType;

	public GetWeaponGoal_Evaluator(Double bias, RavenObject weaponType) {
		super(bias);

		this.weaponType = weaponType;
	}

	public double calculateDesirability(RavenBot pBot)
	{
		//grab the distance to the closest instance of the weapon type
		double Distance = RavenFeature.DistanceToItem(pBot, weaponType);

		//if the distance feature is rated with a value of 1 it means that the
		//item is either not present on the map or too far away to be worth 
		//considering, therefore the desirability is zero
		if (Distance < 0) {
			return 0;
		} else {
			//value used to tweak the desirability
			double Tweaker = 0.15;

			double Health, WeaponStrength;

			Health = RavenFeature.Health(pBot);
			WeaponStrength = RavenFeature.IndividualWeaponStrength(pBot, weaponType);

			double Desirability = (Tweaker * Health * (1-WeaponStrength)) / Distance;

			//ensure the value is in the range 0 to 1
			RavenFeature.Clamp(Desirability, 0, 1);

			Desirability *= getBias();

			return Desirability;
		}
	}



	//------------------------------ SetGoal --------------------------------------
	public void setGoal(RavenBot pBot)
	{
		try {
			pBot.getBrain().addGoal_getItem(weaponType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	//-------------------------- RenderInfo ---------------------------------------
	//-----------------------------------------------------------------------------
	public void RenderInfo(Vector2D Position, RavenBot pBot)
	{
		String s = new String();
		switch(weaponType)
		{
		case RAIL_GUN:
			s="RG: ";break;
		case ROCKET_LAUNCHER:
			s="RL: "; break;
		case SHOTGUN:
			s="SG: "; break;
		case BLASTER:
			s="BS: "; break;
		}

		GameCanvas.textAtPos(Position, s + String.valueOf( (calculateDesirability(pBot))));
	}






}
