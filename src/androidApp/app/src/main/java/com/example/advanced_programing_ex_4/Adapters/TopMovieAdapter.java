//public class TopMovieAdapter extends RecyclerView.Adapter<TopMovieAdapter.TopMovieViewHolder> {
//
//    private List<Movie> topMovies;
//    private Context context;
//
//    public TopMovieAdapter(Context context) {
//        this.context = context;
//        this.topMovies = new ArrayList<>();
//    }
//
//    @Override
//    public TopMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        // Inflate the item layout and return the ViewHolder
//        View view = LayoutInflater.from(context).inflate(R.layout.item_top_movie, parent, false);
//        return new TopMovieViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(TopMovieViewHolder holder, int position) {
//        // Bind the data to the ViewHolder
//        Movie movie = topMovies.get(position);
//        holder.bind(movie);
//    }
//
//    @Override
//    public int getItemCount() {
//        return topMovies.size();
//    }
//
//    public void setTopMovies(List<Movie> topMovies) {
//        this.topMovies = topMovies;
//        notifyDataSetChanged(); // Notify the adapter that data has changed
//    }
//
//    public static class TopMovieViewHolder extends RecyclerView.ViewHolder {
//        // ViewHolder setup
//
//        public TopMovieViewHolder(View itemView) {
//            super(itemView);
//            // Initialize views
//        }
//
//        public void bind(Movie movie) {
//            // Bind the movie data to the views
//        }
//    }
//}
