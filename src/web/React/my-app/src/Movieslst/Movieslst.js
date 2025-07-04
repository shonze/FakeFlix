import Movie from '../Movie/Movie';
import './Movielst.css';
import React, { useRef, useState, useEffect } from 'react';

function Movielst({ Movieslst }) {
    const scrollContainerRef = useRef(null);
    const [isLeftArrowVisible, setIsLeftArrowVisible] = useState(false);
    const [isRightArrowVisible, setIsRightArrowVisible] = useState(true);
    const [totalScrolls, setTotalScrolls] = useState(0);
    const [currentScrollIndex, setCurrentScrollIndex] = useState(0);

    const checkMovielst = () => {
        console.log("THEEEEE movies Are: " + Movieslst);
    };

    useEffect(() => {
        const checkScrollability = () => {
            if (scrollContainerRef.current) {
                const { clientWidth, scrollWidth } = scrollContainerRef.current;

                // Check if total content width exceeds container width
                const isContentOverflowing = scrollWidth > clientWidth;

                const scrollAmount = clientWidth * 0.8; // Scroll by 80% of container width
                const totalChunks = Math.ceil(Movieslst.length * 210 / scrollAmount);

                setTotalScrolls(totalChunks);

                // Update arrow visibility
                setIsLeftArrowVisible(false);
                setIsRightArrowVisible(isContentOverflowing);
            }
        };

        // Check on initial render and when items change
        checkScrollability();

        // Add resize listener to recheck on window resize
        window.addEventListener('resize', checkScrollability);

        return () => {
            window.removeEventListener('resize', checkScrollability);
        };
    }, [Movieslst]);

    const scroll = (direction) => {
        if (scrollContainerRef.current) {
            const { clientWidth, scrollWidth, scrollLeft } = scrollContainerRef.current;
            const scrollAmount = clientWidth * 0.8; // Scroll 80% of container width

            const newScrollPosition = direction === 'left'
                ? scrollLeft - scrollAmount
                : scrollLeft + scrollAmount;

            scrollContainerRef.current.scrollTo({
                left: newScrollPosition,
                behavior: 'smooth'
            });

            const currentChunk = Math.round(newScrollPosition / scrollAmount);

            setCurrentScrollIndex(currentChunk);

            // Update arrow visibility
            setIsLeftArrowVisible(newScrollPosition > 0);
            setIsRightArrowVisible(
                newScrollPosition + clientWidth < scrollWidth
            );
        }
    };
    
    return (
        <div className="position-relative">
            { totalScrolls !== 1 ? (
            <div className="dot-indicator">
                {Array.from({ length: totalScrolls }).map((_, index) => (
                    <div
                        key={index}
                        className={`dot ${index === currentScrollIndex ? "active" : ""}`}
                    />
                ))}
            </div> ) : null
            }
            {isLeftArrowVisible && (
                <button
                    onClick={() => scroll('left')}
                    className="scroll-arrow left-arrow"
                >
                    ←
                </button>
            )}

            <div
                ref={scrollContainerRef}
                className="d-flex overflow-x-auto no-scrollbar"
                style={{
                    scrollBehavior: 'smooth',
                    overscrollBehaviorX: 'contain'
                }}
            >
                {Movieslst.map((movieId) => (
                    <div
                        key={movieId}
                        className="flex-shrink-0 me-3"
                        style={{ minWidth: '200px' }}
                    >
                        <Movie id={movieId} />
                    </div>
                ))}
            </div>
            {isRightArrowVisible && (
                <button
                    onClick={() => scroll('right')}
                    className="scroll-arrow right-arrow"
                >
                    →
                </button>
            )}
        </div>
    );
}

export default Movielst;