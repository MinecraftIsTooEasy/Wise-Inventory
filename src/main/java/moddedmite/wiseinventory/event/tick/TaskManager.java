package moddedmite.wiseinventory.event.tick;

import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import moddedmite.wiseinventory.task.AbstractTimedTask;
import moddedmite.wiseinventory.task.ClientTask;
import net.minecraft.Minecraft;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TaskManager implements IClientTickHandler {
    public static final int TASK_SPEED = 5;

    private static final TaskManager Instance = new TaskManager();

    public static TaskManager getInstance() {
        return Instance;
    }

    private TaskManager() {
    }

    private final LinkedList<ClientTask<?>> taskQueue = new LinkedList<>();

    private final List<AbstractTimedTask> timedTasks = new ArrayList<>();

    public void addTaskToQueue(ClientTask<?> task) {
        if (task != null) {
            this.taskQueue.add(task);
        }
    }

    public void addTimedTask(AbstractTimedTask task) {
        if (task != null) {
            this.timedTasks.add(task);
        }
    }

    @Override
    public void onClientTick(Minecraft mc) {
        for (int i = 0; i < TASK_SPEED; i++) {
            ClientTask<?> poll = this.taskQueue.poll();
            if (poll != null && poll.shouldExecute(mc)) {
                poll.execute(mc);
            }
        }
        this.timedTasks.removeIf(abstractTimedTask -> {
            abstractTimedTask.onClientTick(mc);
            if (abstractTimedTask.shouldExecute(mc)) {
                abstractTimedTask.execute(mc);
                return true;
            } else {
                return false;
            }
        });
    }
}
