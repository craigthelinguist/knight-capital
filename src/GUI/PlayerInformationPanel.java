package GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import tools.GlobalConstants;
import tools.ImageLoader;

/**
 * This panel holds all of the player's information such as stats, party  etc.
 //TODO JLabels to display player information
 * 
 * @author Ewan Moshi
 *
 */
public class PlayerInformationPanel extends JPanel{

	private BufferedImage backgroundImage;
	
	public PlayerInformationPanel() {
		/*set the size of this panel to be size of the image*/
		this.setPreferredSize(new Dimension(375,200));
		this.setOpaque(true);
		/*Initialize the image for the inventory panel*/
		backgroundImage = ImageLoader.load(GlobalConstants.GUI_FILEPATH + "playerInfoPanel.png");	
	}
	
	  @Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    // paint the background image and scale it to fill the entire space
	    g.drawImage(backgroundImage, 0, 0, 375, 200, this);
	  }
	
	
}