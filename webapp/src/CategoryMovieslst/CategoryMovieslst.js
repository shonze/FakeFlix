import Movie from '../Movie/Movie';

function CategoryMovielst({ CategoryName, Movieslst }) {
    return (
        <div key={CategoryName} className="sticky-left">
            <h6 className="text-start text-light">{CategoryName}</h6>
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