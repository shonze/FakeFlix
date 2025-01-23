import Movie from '../Movie/Movie';

function Movielst({ Movieslst }) {
    return (
        <>
            <div className="row">
                {Movieslst.map((movieId) => (
                    <div key={movieId} className="col-md-3 mb-4">
                        <Movie id={movieId} />
                    </div>
                ))}
            </div>
        </>
    );
}

export default Movielst;