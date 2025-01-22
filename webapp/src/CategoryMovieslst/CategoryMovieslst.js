import Movie from '../Movie/Movie';

function CategoryMovielst(CategoryName, Movieslst) {    
    return (
        <div>
            <h1>{CategoryName}</h1>
            {
                Movieslst.map(movieId => {
                    <Movie id={movieId} />
                })
            }
        </div>
    );
};

export default CategoryMovielst;