package ShootingGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Game extends Thread {
	private int delay = 20;	// ������ ���� �ֱ�
	private long pretime;
	private int cnt;	// 0.02�ʸ��� ����
	private boolean isOver = false;	// ���� ���� ����
	
	private Image player = new ImageIcon("src/images/player.png").getImage();
	
	private int playerX, playerY;	// �÷��̾� ��ġ
	private int playerWidth = player.getWidth(null);
	private int playerHeight = player.getHeight(null);	// �÷��̾� ����, ���� ũ��
	private int playerSpeed = 10;
	private int playerHp = 30;

	private boolean up, down, left, right, shooting;	// Ű ����
	
	ArrayList<PlayerAttack> playerAttackList = new ArrayList<PlayerAttack>();
	ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	ArrayList<EnemyAttack> enemyAttackList = new ArrayList<EnemyAttack>();
	private PlayerAttack playerAttack;
	private Enemy enemy;
	private EnemyAttack enemyAttack;
	
	@Override
	public void run() {
		cnt = 0;
		playerX = 10;
		playerY = (1080 - playerHeight) / 2;
		
		while (!isOver) {
			pretime = System.currentTimeMillis();
			if (System.currentTimeMillis() - pretime < delay) {
				try {
					Thread.sleep(delay - System.currentTimeMillis() + pretime);
					keyProcess();
					playerAttackProcess();
					enemyAppearProcess();
					enemyMoveProcess();
					enemyAttackProcess();
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
		if (shooting && cnt % 15 == 0) {
			playerAttack = new PlayerAttack(playerX + 222, playerY + 35);
			playerAttackList.add(playerAttack);
		}	// 0.4�ʸ��� �̻��� �߻�
	}
	
	// �÷��̾��� ������ ó�����ִ� �޼ҵ�
	private void playerAttackProcess() {
		for (int i = 0; i < playerAttackList.size(); i++) {
			playerAttack = playerAttackList.get(i);
			playerAttack.fire();
			
			for (int j = 0; j < enemyList.size(); j++) {
				enemy = enemyList.get(j);
				if (playerAttack.x > enemy.x && playerAttack.x < enemy.x + enemy.width && playerAttack.y > enemy.y && playerAttack.y < enemy.y + enemy.height) {
					enemy.hp -= playerAttack.attack;
					playerAttackList.remove(playerAttack);
				}	// �浹 ���� �� ������ ���������� ü�� �÷��̾� ���ݷ¸�ŭ ü�� ���ҽ�Ű��
				if (enemy.hp <= 0) {
					enemyList.remove(enemy);	// ü���� ������ ����
				}
			}
		}
	}
	
	// ���� ������ ó�����ִ� �޼ҵ�
	private void enemyAppearProcess() {
		if (cnt % 80 == 0) {
			int enemyY = (int)(Math.random() * 621);
			enemy = new Enemy(1120, enemyY);
			enemyList.add(enemy);
		}
	}
	
	// ���� �������� ó�����ִ� �޼ҵ�
	private void enemyMoveProcess() {
		for (int i = 0; i < enemyList.size(); i++) {
			enemyList.get(i).move();
		}
	}
	
	// ���� ������ ó�����ִ� �޼ҵ�
	private void enemyAttackProcess() {
		if (cnt % 50 == 0) {
			enemyAttack = new EnemyAttack(enemy.x - 79, enemy.y + 35);
			enemyAttackList.add(enemyAttack);
		}
		
		for (int i = 0; i < enemyAttackList.size(); i++) {
			enemyAttack = enemyAttackList.get(i);
			enemyAttack.fire();
			
			if (enemyAttack.x > playerX && enemyAttack.x < playerX + playerWidth && enemyAttack.y > playerY && enemyAttack.y < playerY + playerHeight) {
				playerHp -= enemyAttack.attack;
				enemyAttackList.remove(enemyAttack);
			}	// �浹 ���� �� ������ ���������� �÷��̾� ü�� ���� ���ݷ¸�ŭ ü�� ���ҽ�Ű��
			if (playerHp <= 0) {
				isOver = true;
				System.out.println("���� ����");
			}
		}
	}
	
	// ���ӿ� �ʿ��� �͵��� �׷��ִ� �޼ҵ�
	public void gameDraw(Graphics g) {
		playerDraw(g);
		enemyDraw(g);
	}
	
	// �÷��̾�� �÷��̾��� ������ �׷��ִ� �޼ҵ�
	public void playerDraw(Graphics g) {
		g.drawImage(player, playerX, playerY, null);
		g.setColor(Color.GREEN);
		g.fillRect(playerX - 1, playerY - 40, playerHp * 6, 20);
		for (int i = 0; i < playerAttackList.size(); i++) {
			playerAttack = playerAttackList.get(i);
			g.drawImage(playerAttack.image, playerAttack.x, playerAttack.y, null);
		}
	}
	
	// ���� ���� ������ �׷��ִ� �޼ҵ�
	public void enemyDraw(Graphics g) {
		for (int i = 0; i < enemyList.size(); i++) {
			enemy = enemyList.get(i);
			g.drawImage(enemy.image, enemy.x, enemy.y, null);
			g.setColor(Color.GREEN);
			g.fillRect(enemy.x + 1, enemy.y - 40, enemy.hp * 15, 20);
		}
		for (int i = 0; i < enemyAttackList.size(); i++) {
			enemyAttack = enemyAttackList.get(i);
			g.drawImage(enemyAttack.image, enemyAttack.x, enemyAttack.y, null);
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
