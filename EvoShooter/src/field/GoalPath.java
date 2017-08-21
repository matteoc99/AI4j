package field;

import math.Position;

import java.util.ArrayList;

/**
 * @author Maximilian Estfelller
 * @since 20.08.2017
 */
class GoalPath implements Cloneable {

    private ArrayList goals = new ArrayList<Position>();

    GoalPath(Position lastGoal) {
        goals.add(lastGoal);
    }

    void addGoal(Position goal) {
        goals.add(goal);
    }

    void goalReached() {
        goals.remove(goals.size()-1);
    }

    public Object clone() {
        try {
            GoalPath v = (GoalPath)super.clone();
            v.goals = (ArrayList) goals.clone();
            return v;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new InternalError();
        }
    }
}
