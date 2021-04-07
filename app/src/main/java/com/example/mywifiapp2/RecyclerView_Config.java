//package com.example.mywifiapp2;
//
//import android.content.Context;
//import android.service.autofill.LuhnChecksumValidator;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.mywifiapp2.wapcollector.Fingerprint;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RecyclerView_Config {
//    private Context mContext;
//    private FingerprintAdapter fingerprintAdapter;
//
//    public void setConfig(RecyclerView recyclerView, Context context, List<Fingerprint> fingerprintList){
//        mContext = context;
//        fingerprintAdapter = new FingerprintAdapter(fingerprintList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setAdapter(fingerprintAdapter);
//    }
//
//    class FingerprintItemView extends RecyclerView.ViewHolder{
//        private Button fingerprintItem;
//        private List wifiData;
//
//        public FingerprintItemView(ViewGroup parent){
//            super(LayoutInflater.from(mContext).inflate(R.layout.fingerprint_item, parent, false));
//            fingerprintItem = (Button) itemView.findViewById(R.id.fingerprint_item);
//        }
//
//        public void bind(Fingerprint fingerprint){
//            fingerprintItem.setText(fingerprint.getName());
//        }
//    }
//
//    class FingerprintAdapter extends RecyclerView.Adapter<FingerprintItemView>{
//        private List<Fingerprint> fingerprintList;
//
//        public FingerprintAdapter (List<Fingerprint> fingerprintList){
//            this.fingerprintList = fingerprintList;
//        }
//
//        @NonNull
//        @Override
//        public FingerprintItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return new FingerprintItemView(parent);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull FingerprintItemView holder, int position) {
//            holder.bind(fingerprintList.get((position)));
//        }
//
//        @Override
//        public int getItemCount() {
//            return fingerprintList.size();
//        }
//    }
//}
