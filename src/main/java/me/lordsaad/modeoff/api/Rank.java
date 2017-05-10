package me.lordsaad.modeoff.api;

import java.awt.*;

/**
 * Created by LordSaad.
 */
public class Rank {

	public final String name;
	public final Color color;
	public final boolean gm1;
	public final int claimablePlots;
	public final boolean editOthers;

	public Rank(String name, Color color, boolean gm1, int claimablePlots, boolean editOthers) {
		this.name = name;
		this.color = color;
		this.gm1 = gm1;
		this.claimablePlots = claimablePlots;
		this.editOthers = editOthers;
	}
}
