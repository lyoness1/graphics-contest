/*
 * File: GraphicsContest.java
 * --------------------------
 * DUCK HUNT
 * Name:Allison Lyon
 * Section Leader: Isabelle Kim
 * Description of entry:
 * 
 * 
 * NOTE #1: All Duck Hunt images copied from www.retrogamezone.co.uk., 
 * http://www.consoleclassix.com/gameinfo_duckhunt_nes.html, and 
 * http://freewebarcade.com/arcade/general/duckhunt/2.gif.  
 * All origianl Duck Hunt sounds and music downlaoded from 
 * http://www.vgmusic.com/.  Other sounds downloaded from
 * http://www.a1freesoundeffects.com/noflash.htm.  
 * 
 * NOTE #2: Everything I know about computers I learned in CS105 or CS106A.  
 * Also, I had to learn photoshop to do this project.  I'm impressed with myself : )
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class GraphicsContest extends GraphicsProgram {
	
/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 1000;
	public static final int APPLICATION_HEIGHT = 800;

/** Dimensions of game board*/
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;
	
	private GImage titleBackground = new GImage("Title.jpg");
	private GImage gameA = new GImage("GameA.jpg");
	private GLabel click = new GLabel("C l i c k  t o  p l a y");
	private GImage backgroundImage = new GImage("GoodBackgroundNoScore.gif");
	private GImage grassHeight = new GImage("GrassHeightWithScore.gif");
	private GImage dog = new GImage("dog1.gif");
	private GImage duck = new GImage("DuckRight1.gif");
	private double vx, vy;
	private RandomGenerator rgen = new RandomGenerator();
	private int spins = 0; 	
	private GObject shot;
	private double lastX, lastY;
	private AudioClip quackingDuck = MediaTools.loadAudioClip("duckquack.wav");
	private AudioClip intro = MediaTools.loadAudioClip("Duck_Hunt.wav");
	private AudioClip gun = MediaTools.loadAudioClip("shotgun2.wav");
	private AudioClip bark = MediaTools.loadAudioClip("bark.wav");
	private int shotsTaken;
	private int flyAways = 0;
	private int duckNumber = -1;
	int score = 0;
	private GLabel totalScore = new GLabel("" + score);
	private GOval bullet1 = new GOval(12, 25);
	private GOval bullet2 = new GOval(12, 25);
	private GOval bullet3 = new GOval(12, 25);
	private int bulletsShot = 0;
	
	public void run() {
		displayIntro(); 
		addMouseListeners();
		waitForClick();
		displayBackground();
		walkDogIn(); 
		playDuckHunt();		
	}

	public void mouseClicked(MouseEvent e){
		bulletsShot++;
		lastX = e.getX();
		lastY = e.getY();
		if(bulletsShot < 4){
			shot = getElementAt(lastX, lastY);
			gun.play();
			if(bulletsShot == 1){
				remove(bullet1);
			}
			if(bulletsShot == 2){
				remove(bullet2);
			}
			if(bulletsShot == 3){
				remove(bullet3);
			}
		}
		if(bulletsShot > 3){
			shot = null;
			AudioClip emptyChamber = MediaTools.loadAudioClip("emptychamber.wav.htm");
			emptyChamber.play();
		}
	}

	private void displayIntro(){
		add(titleBackground);
		intro.play();
		add(gameA, WIDTH/2 - gameA.getWidth()/2, HEIGHT - 300);
		click.setFont(new Font("COURIER", Font.PLAIN, 40));
		click.setColor(WHITE);
		add(click, WIDTH/2 - click.getWidth()/2, HEIGHT - 50);
	}
	
	private void displayBackground(){
		remove(titleBackground);
		remove(gameA);
		remove(click);
		intro.stop();
		add(backgroundImage);
	}
	
	private void walkDogIn(){
		add(dog, -dog.getWidth(), HEIGHT - dog.getHeight() - 100);
		AudioClip WalkingDog = MediaTools.loadAudioClip("duckhuntdog.wav");
		WalkingDog.play();
		while(dog.getX() < (WIDTH - dog.getWidth())/2){			
			dog.move(15,0);
			pause(100);
			dog.setImage("dog2.gif");
			dog.move(15,0);
			pause(100);
			dog.setImage("dog3.gif");
			dog.move(15,0);
			pause(100);
			dog.setImage("dog4.gif");
			dog.move(15,0);
			pause(100);
			dog.setImage("dog5.gif");
			dog.move(15,0);
			pause(100);
			dog.setImage("dog1.gif");
		}
		bark.play();
		while(dog.getY() > HEIGHT - 280- dog.getHeight()){
			dog.setImage("JumpingDog.gif");
			dog.move(0, -5);
			pause(20);
		}
		addBottom();
		while(dog.getY() < HEIGHT - 200){
			dog.move(0, 5);
			pause(20);
		}
		remove(dog);
	}
	
	private void addBottom(){
		add(grassHeight, 0, HEIGHT - grassHeight.getHeight());
		totalScore.setFont(new Font("COURIER", Font.BOLD, 36));
		totalScore.setColor(WHITE);
		add(totalScore, 860 - totalScore.getWidth(), HEIGHT - 65);
	}
	
	private void playDuckHunt(){
		duckNumber++;
		bulletsShot = 0;
		if(duckNumber < 8){
			spins = 0;
			addBullets();
			int duckStartPosition = rgen.nextInt(0, 864);
			add(duck, duckStartPosition, HEIGHT - 282);
			duck.sendToBack();
			duck.sendForward();
			quackingDuck.play();
			moveDuck();
		}
		if(duckNumber == 8){
			if(flyAways <= 3){
				arrangeDucks();
				win();
			}else if(flyAways > 3){
				arrangeDucks();
				gameOver();
			}
		}
	}
	
	private void addBullets(){
		remove(bullet1);
		remove(bullet2);
		remove(bullet3);
		bullet1.setColor(LIGHT_GRAY);
		bullet1.setFilled(true);
		bullet2.setColor(LIGHT_GRAY);
		bullet2.setFilled(true);
		bullet3.setColor(LIGHT_GRAY);
		bullet3.setFilled(true);
		add(bullet1, 160, HEIGHT - 85);
		add(bullet2, 130, HEIGHT - 85);
		add(bullet3, 100, HEIGHT - 85);
	}
		
	private void moveDuck(){
		if(spins < 4){
			spins++;
			int i = rgen.nextInt(0, 9);
			if(i == 0){
				flyRight();
			}else if(i == 0){
				flyLeft();
			}else if(i == 1){
				flyUp();
			}else if(i == 2){
				flyDown();
			}else if(i == 3 || i == 4){
				flyDownRight();
			}else if(i == 5 || i == 6){
				flyDownLeft();
			}else if(i == 7 || i == 8){
				flyUpRight();
			}else if(i == 9 || i == 10){
				flyUpLeft();
			}
		}else if(spins == 4){
			flyAway();
			spins++;
		}
	}
	
	private void flyAway(){
		backgroundImage.setImage("FlyAwayBackground.gif");
		GLabel flyAway = new GLabel("FLY AWAY");
		flyAway.setFont(new Font("ARIAL", Font.PLAIN, 40));
		add(flyAway, WIDTH/2 - flyAway.getWidth()/2, 300);
		addWhiteScoringDuck();
		while(duck.getY() > -duck.getHeight()){
			duck.setImage("DuckUp1.gif");
			duck.move(0, -20);
			pause(70);
			duck.setImage("DuckUp2.gif");
			duck.move(0, -20);
			pause(70);
			duck.setImage("DuckUp3.gif");
			duck.move(0, -20);
			pause(70);
		}
		remove(duck);
		backgroundImage.setImage("GoodBackgroundNoScore.gif");
		dogLaugh();
		remove(flyAway);
		flyAways++;
		playDuckHunt();
	}
	
	private void addWhiteScoringDuck(){
		GImage white = new GImage("ScoringDuckWhite.gif");
		add(white, 370 + 40*duckNumber, HEIGHT - 85);
	}

	private void addRedScoringDuck(){
		GImage red = new GImage("ScoringDuckRed.gif");
		add(red, 370 + 40*duckNumber, HEIGHT - 85);
	}
	
	private void arrangeDucks(){
		for(int j = 0; j < (8 - flyAways); j++){
			GImage red = new GImage("ScoringDuckRed.gif");
			add(red, 370 + 40*j, HEIGHT - 85);
		}
		for(int k = (8 - flyAways); k < 8; k++){
			GImage white = new GImage("ScoringDuckWhite.gif");
			add(white, 370 + 40*k, HEIGHT - 85);
		}
	}
	
	private void win(){
		GLabel win = new GLabel("YOU WIN!!");
		win.setFont(new Font("ARIAL", Font.PLAIN, 40));
		add(win, WIDTH/2 - win.getWidth()/2, 300);
	}
	
	private void gameOver(){
		GLabel gameOver = new GLabel("GAME OVER");
		gameOver.setFont(new Font("ARIAL", Font.PLAIN, 40));
		add(gameOver, WIDTH/2 - gameOver.getWidth()/2, 300);
	}
	
	private void flyRight(){
		vx = 20;
		vy = 0;
		int n = 0;
		while(true){
			duck.setImage("DuckRight1.gif");
			duck.move(vx, vy);
			checkR();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckRight2.gif");
			duck.move(vx, vy);
			checkR();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckRight3.gif");
			duck.move(vx, vy);
			checkR();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			n++;
			if(n == 3){
				moveDuck();
				n++;
			}
		}
		playDuckHunt();
	}
	
	private void checkR(){
		if(duck.getX() > 1000 - duck.getWidth()){
			flyLeft();
		}
	}
	
	private void flyLeft(){
		vx = -20;
		vy = 0;
		int n = 0;
		while(true){
			duck.setImage("DuckLeft1.gif");
			duck.move(vx, vy);
			checkL();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckLeft2.gif");
			duck.move(vx, vy);
			checkL();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckLeft3.gif");
			duck.move(vx, vy);
			checkL();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			n++;
			if(n == 3){
				moveDuck();
				n++;
			}
		}
		playDuckHunt();
	}
	
	private void checkL(){
		if(duck.getX() < 0){
			flyRight();
		}
	}
	
	private void flyUp(){
		vx = 0;
		vy = -20;
		int n = 0;
		while(true){
			duck.setImage("DuckUp1.gif");
			duck.move(vx, vy);
			checkUp();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckUp2.gif");
			duck.move(vx, vy);
			checkUp();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckUp3.gif");
			duck.move(vx, vy);
			checkUp();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			n++;
			if(n == 3){
				moveDuck();
				n++;
			}
		}
		playDuckHunt();
	}
	
	private void checkUp(){
		if(duck.getY() < 0){
			flyDown();
		}
	}
	
	private void flyDown(){
		vx = 0;
		vy = 20;
		int n = 0;
		while(true){
			duck.setImage("DuckDown1.gif");
			duck.move(vx, vy);
			checkDown();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckDown2.gif");
			duck.move(vx, vy);
			checkDown();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckDown3.gif");
			duck.move(vx, vy);
			checkDown();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			n++;
			if(n == 3){
				moveDuck();
				n++;
			}
		}
		playDuckHunt();
	}
	
	private void checkDown(){
		if(duck.getY() > HEIGHT - 280 - duck.getHeight()){
			flyUp();
		}
	}

	private void flyDownRight(){
		vx = 20;
		vy = 20;
		int n = 0;
		while(true){
			duck.setImage("DuckRight1.gif");
			duck.move(vx, vy);
			checkDR();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckRight2.gif");
			duck.move(vx, vy);
			checkDR();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckRight3.gif");
			duck.move(vx, vy);
			checkDR();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			n++;
			if(n == 3){
				moveDuck();
				n++;
			}
		}
		playDuckHunt();
	}
	
	private void checkDR(){
		if(duck.getX() > 1000 - duck.getWidth()){
			flyDownLeft();
		}
		if(duck.getY() > HEIGHT - 280 - duck.getHeight()){
			flyUpRight();
		}
	}
	
	private void flyDownLeft(){
		vx = -20;
		vy = 20;
		int n = 0;
		while(true){
			duck.setImage("DuckLeft1.gif");
			duck.move(vx, vy);
			checkDL();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckLeft2.gif");
			duck.move(vx, vy);
			checkDL();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckLeft3.gif");
			duck.move(vx, vy);
			checkDL();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			n++;
			if(n == 3){
				moveDuck();
				n++;
			}
		}
		playDuckHunt();
	}
	
	private void checkDL(){
		if(duck.getX() < 0){
			flyDownRight();
		}
		if(duck.getY() > HEIGHT - 280 - duck.getHeight()){
			flyUpLeft();
		}
	}
	
	private void flyUpRight(){
		vx = 20;
		vy = -20;
		int n = 0;
		while(true){
			duck.setImage("DuckUpRight1.gif");
			duck.move(vx, vy);
			checkUR();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckUpRight2.gif");
			duck.move(vx, vy);
			checkUR();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckUpRight3.gif");
			duck.move(vx, vy);
			checkUR();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			n++;
			if(n == 3){
				moveDuck();
				n++;
			}
		}
		playDuckHunt();
	}
	
	private void checkUR(){
		if(duck.getX() > 1000 - duck.getWidth()){
			flyUpLeft();
		}
		if(duck.getY() < 0){
			flyDownRight();
		}
	}
	
	private void flyUpLeft(){
		vx = -20;
		vy = -20;
		int n = 0;
		while(true){
			duck.setImage("DuckUpLeft1.gif");
			duck.move(vx, vy);
			checkUL();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckUpLeft2.gif");
			duck.move(vx, vy);
			checkUL();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			duck.setImage("DuckUpLeft3.gif");
			duck.move(vx, vy);
			checkUL();
			if(shot == duck){
				duck.setImage("DuckShot.gif");
				killDuck();
				break;
			}
			pause(70);
			n++;
			if(n == 3){
				moveDuck();
				n++;
			}
		}
		playDuckHunt();
	}
	
	private void checkUL(){
		if(duck.getX() < 0){
			flyUpRight();
		}
		if(duck.getY()< 0){
			flyDownLeft();
		}
	}
	
	private void killDuck(){
		shot = null;
		spins++;
		addScore();
		quackingDuck.stop();
		addRedScoringDuck();
		pause(400);
		duckFall();
		dogUp();
	}
	
	private void addScore(){
		score += 1000;
		totalScore.setLabel("" + score);
	}
		
	private void duckFall(){
		duck.setImage("DuckFall.gif");
		while(duck.getY() < HEIGHT - 280){
			duck.move(0, 10);
			pause(20);
		}
		remove(duck);
	}
	
	private void dogUp(){
		dog.setImage("DogWithDuck.gif");
		add(dog, WIDTH/2 - dog.getWidth()/2, HEIGHT - grassHeight.getHeight());
		dog.sendToBack();
		dog.sendForward();
		while(dog.getY() > HEIGHT - 260 - dog.getHeight()){ 
			dog.move(0, -20);
			pause(50);
		}
		bark.play();
		pause(2000);
		while(dog.getY() < HEIGHT - 200){
			dog.move(0, 20);
			pause(50);
		}
		remove(dog);
	}
	
	private void dogLaugh(){
		dog.setImage("LaughingDog.gif");
		add(dog, WIDTH/2 - dog.getWidth()/2, HEIGHT - grassHeight.getHeight());
		dog.sendToBack();
		dog.sendForward();
		while(dog.getY() > HEIGHT - 260 - dog.getHeight()){ 
			dog.move(0, -20);
			pause(50);
		}
		AudioClip laugh = MediaTools.loadAudioClip("giddylaugh.wav");
		laugh.play();
		pause(2000);
		while(dog.getY() < HEIGHT - 200){
			dog.move(0, 20);
			pause(50);
		}
		remove(dog);
	}
}

