package ShootingGame;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Game extends Thread {
	private int delay = 20;	// ������ ���� �ֱ�
	private long pretime;
	
	private int cnt;	// 0.02�ʸ��� ����
	
	private Image player = new ImageIcon("src/images/player.png").getImage();
	
	private int playerX, playerY;	// �÷��̾� ��ġ
	private int playerWidth = player.getWidth(null);
	private int playerHeight = player.getHeight(null);	// �÷��̾� ����, ���� ũ��
	private int playerSpeed = 10;

	private boolean up, down, left, right, shooting;	// Ű ����
	
	ArrayList<PlayerAttack> playerAttackList = new ArrayList<PlayerAttack>();
	private PlayerAttack playerAttack;
	
	@Override
	public void run() {
		cnt = 0;
		playerX = 10;
		playerY = (1080 - playerHeight) / 2;
		
		while (true) {
			pretime = System.currentTimeMillis();
			if (System.currentTimeMillis() - pretime < delay) {
				try {
					Thread.sleep(delay - System.currentTimeMillis() + pretime);
					keyProcess();
					playerAttackProcess();
					cnt++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// �÷��̾� �������� �޾Ƶ��̴� �޼ҵ�
	private void keyProcess() {
		if (up && playerY - playerSpeed > 0) playerY -= 10;
		if (down && playerY + playerHeight + playerSpeed < Main.SCREEN_HEIGHT) playerY += 10;
		if (left && playerX - playerSpeed > 0) playerX -= 10;
		if (right && playerX + playerWidth + playerSpeed < Main.SCREEN_WIDTH) playerX += 10;
		if (shooting && cnt % 20 == 0) {
			playerAttack = new PlayerAttack(playerX + 222, playerY + 35);
			playerAttackList.add(playerAttack);
		}	// 0.4�ʸ��� �̻��� �߻�
	}
	
	// �÷��̾��� ������ ó�����ִ� �޼ҵ�
	private void playerAttackProcess() {
		for (PlayerAttack attack : playerAttackList) {
			attack.fire();
		}
	}
	
	// ���ӿ� �ʿ��� �͵��� �׷��ִ� �޼ҵ�
	public void gameDraw(Graphics g) {
		playerDraw(g);
	}
	
	// �÷��̾�� �÷��̾��� ������ �׷��ִ� �޼ҵ�
	public void playerDraw(Graphics g) {
		g.drawImage(player, playerX, playerY, null);
		for (PlayerAttack attack : playerAttackList) {
			g.drawImage(attack.attackImage, attack.x, attack.y, null);
		}
	}
	
	// ����Ű ��� ���� setter
	public void setUp(boolean up) {
		this.up = up;
	}
		
	public void setDown(boolean down) {
		this.down = down;
	}
		
	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}
}
