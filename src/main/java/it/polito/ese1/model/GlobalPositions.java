package it.polito.ese1.model;

import java.util.LinkedList;
import java.util.List;

public class GlobalPositions {

    private final List<GlobalPosition> positions = new LinkedList<GlobalPosition>();

    public void addPositions(List<GlobalPosition> positions) {
        int num_positions = positions.size();
        switch (num_positions) {
            case 0: throw new IllegalArgumentException();
            case 1: this.positions.add(positions.get(0));
            default:
                GlobalPosition reference = positions.get(0);
                for (int i = 1; i < num_positions; i++) {
                    GlobalPosition current = positions.get(i);
                    if (!current.getTimestamp().after(reference.getTimestamp())) {
                        throw new IllegalArgumentException();
                    }
                    reference = current;
                }
                this.positions.addAll(positions);

        }
    }

    public List<GlobalPosition> getPositions() {
        return this.positions;
    }
}
