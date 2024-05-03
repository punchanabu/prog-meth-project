package main.game.boss;
public class TrollBoss extends Boss {
    private boolean isRaging;
    private boolean isBerserk;

    public TrollBoss(String name, int health, int damage) {
        super(name, health, damage);
        this.isRaging = false;
        this.isBerserk = false;
    }

    public boolean isRaging() {
        return isRaging;
    }

    public boolean isBerserk() {
        return isBerserk;
    }

    @Override
    public void specialAttack() {
        if (isRaging) {
            System.out.println(getName() + " smashes the ground with tremendous force!");
        } else {
            System.out.println(getName() + " roars angrily and enters a rage!");
            isRaging = true;
        }
    }

    @Override
    public void ultimateAttack() {
        if (isRaging) {
            System.out.println(getName() + " unleashes a devastating frenzy of attacks!");
            isRaging = false;
        } else {
            System.out.println(getName() + " prepares to unleash its ultimate power!");
        }
    }

    // Special ability: Berserk Strike
    public void berserkStrike() {
        if (!isBerserk) {
            System.out.println(getName() + " enters a berserk state, increasing damage!");
            isBerserk = true;
        } else {
            System.out.println(getName() + " is already in a berserk state!");
        }
    }

    @Override
    public int getDamage() {
        if (isBerserk) {
            return super.getDamage() * 2;
        } else {
            return super.getDamage();
        }
    }
}
