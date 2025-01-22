function Movie(id) {
    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-8">
                    <div className="image-container">
                        <img src="your-image-url-here" alt="Brooklyn 99" />
                        <div className="overlay-text">
                            ברוקלין<br />
                            תשע-תשע
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Movie;