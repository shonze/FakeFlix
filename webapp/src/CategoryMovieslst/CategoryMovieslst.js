import Movie from '../Movie/Movie';

function CategoryMovielst({ CategoryName, Movieslst }) {
    return (
        <div key={CategoryName}>
            <h2 className="text-center">{CategoryName}</h2>
            <div className="row">
                {Movieslst.map((movieId) => (
                    <div key={movieId} className="col-md-3 mb-4">
                        <Movie id={movieId} />
                    </div>
                ))}
            </div>
        </div>
    );
}

export default CategoryMovielst;