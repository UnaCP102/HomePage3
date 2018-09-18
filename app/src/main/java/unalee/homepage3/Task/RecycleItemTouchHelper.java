package unalee.homepage3.Task;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

public class RecycleItemTouchHelper extends ItemTouchHelper.Callback{
    private RecyclerView recyclerView;
    private static final String TAG ="RecycleItemTouchHelper" ;
    private final RecyclerView helperCallback;

    public RecycleItemTouchHelper(RecyclerView helperCallback) {
        this.helperCallback = (RecyclerView) helperCallback;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.LEFT;
        int dragFlags = ItemTouchHelper.RIGHT;
        Log.e(TAG, "getMovementFlags: " );
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    }

    private interface ItemTouchHelperCallback {
        void onItemDelete(int positon);
        void onMove(int fromPosition,int toPosition);
    }
}
