package am.newway.aca.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import am.newway.aca.R;
import am.newway.aca.anim.RecyclerViewAnimator;
import am.newway.aca.template.Notification;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public
class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> notifications;
    private int animPosition = -1;
    private Context context;
    private RecyclerViewAnimator mAnimator;

    public
    NotificationAdapter ( Context context , RecyclerViewAnimator mAnimator ) {
        this.mAnimator = mAnimator;
        this.context = context;
        notifications = new ArrayList<>();
    }

    public
    void setNotifications ( List<Notification> notifications ) {
        if ( notifications != null ) {
            this.notifications.clear();
            this.notifications.addAll( notifications );
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public
    ViewHolder onCreateViewHolder ( @NonNull final ViewGroup parent , int viewType ) {
        View view;
        view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.notification_item , parent , false );

        mAnimator.onCreateViewHolder( view );

        return new ViewHolder( view );
    }

    @Override
    public
    void onBindViewHolder ( @NonNull ViewHolder holder , int position ) {
        if ( animPosition != -1 ) {
            final Animation myAnim = AnimationUtils.loadAnimation( context , R.anim.bounce );
            holder.itemView.setAnimation( myAnim );
            animPosition = -1;
        }
        else
            mAnimator.onBindViewHolder( holder.itemView , position );
        holder.bind( notifications.get( position ) );
    }

    @Override
    public
    int getItemViewType ( int position ) {
        return super.getItemViewType( position );
    }

    @Override
    public
    int getItemCount () {
        return notifications.size();
    }

    public
    Notification getNotification ( final int i ) {
        return notifications.get( i );
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDescription;
        TextView textDate;
        SimpleDraweeView imageView;
        Notification notification;
        String[] type =
                context.getResources().getStringArray( R.array.notification_type );
        PorterDuffColorFilter colorFilter =
                new PorterDuffColorFilter( context.getResources().getColor(R.color.colorPrimaryLight),
                        PorterDuff.Mode.SRC_IN);

        ViewHolder ( @NonNull final View itemView ) {
            super( itemView );

            textDate = itemView.findViewById( R.id.date_time);
            textDescription = itemView.findViewById( R.id.text_description );
            textName = itemView.findViewById( R.id.text_name );
            imageView = itemView.findViewById( R.id.imageView );
        }

        void bind ( Notification notification ) {

            synchronized ( this ) {

                this.notification = notification;

                textName.setText( type[notification.getMessageType()] );
                textDescription.setText( notification.getMessage() );
                Date date = notification.getDate();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm   dd.MM.yyyy");
                String textD = format.format( date );
                textDate.setText( textD );
                if ( imageView != null ) {
                    imageView.setColorFilter( colorFilter );
                    switch ( notification.getMessageType() ) {
                        case 0:
                            imageView.setImageResource( R.drawable.message ); break;
                        case 1:
                            imageView.setImageResource( R.drawable.alert ); break;
                        case 2:
                            imageView.setImageResource( R.drawable.news ); break;
                    }
                }
            }
        }
    }
}
