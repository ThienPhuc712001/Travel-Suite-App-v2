import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { MapPin, Star } from 'lucide-react';

const Home = () => {
  const { API_BASE_URL } = useAuth();
  const [places, setPlaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchPlaces();
  }, []);

  const fetchPlaces = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/places`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        setPlaces(data.slice(0, 3)); // Display top 3 places on home page
      } else {
        setError('Failed to fetch places');
      }
    } catch (err) {
      setError('Network error: Could not connect to API');
      console.error('Error fetching places:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <section className="bg-blue-600 text-white py-20 text-center">
        <h1 className="text-5xl font-bold mb-4">Discover Amazing Places</h1>
        <p className="text-xl mb-8">Share your travel experiences and explore destinations from fellow travelers</p>
        <div className="max-w-md mx-auto flex bg-white rounded-lg shadow-lg overflow-hidden">
          <input
            type="text"
            placeholder="Search for places..."
            className="flex-grow p-4 text-gray-800 focus:outline-none"
          />
          <button className="bg-blue-700 hover:bg-blue-800 text-white px-6 py-4">
            Search
          </button>
        </div>
      </section>

      {/* Featured Places Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-3xl font-bold text-gray-900">Featured Places</h2>
          <Link to="/places" className="text-blue-600 hover:text-blue-800 font-medium">
            View all places &rarr;
          </Link>
        </div>

        {loading ? (
          <div className="text-center py-12">Loading featured places...</div>
        ) : error ? (
          <div className="text-center py-12 text-red-600">Error: {error}</div>
        ) : places.length === 0 ? (
          <div className="text-center py-12">
            <MapPin className="mx-auto h-12 w-12 text-gray-400" />
            <h3 className="mt-2 text-sm font-medium text-gray-900">No places found</h3>
            <p className="mt-1 text-sm text-gray-500">
              Be the first to add a place!
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {places.map((place) => (
              <Link
                key={place.id}
                to={`/places/${place.id}`}
                className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow"
              >
                <div className="h-48 bg-gradient-to-r from-blue-400 to-blue-600 flex items-center justify-center">
                  <MapPin className="h-16 w-16 text-white opacity-50" />
                </div>
                <div className="p-6">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">{place.name}</h3>
                  <p className="text-gray-600 text-sm mb-4 line-clamp-2">{place.description}</p>
                  <div className="flex items-center justify-between text-sm text-gray-500">
                    <div className="flex items-center">
                      <MapPin className="h-4 w-4 mr-1" />
                      {place.location}
                    </div>
                    <div className="flex items-center">
                      <Star className="h-4 w-4 mr-1 text-yellow-400" />
                      <span>{place.averageRating ? place.averageRating.toFixed(1) : 'N/A'}</span>
                    </div>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}
      </section>

      {/* Stats Section */}
      <section className="bg-gray-100 py-12 text-center">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="flex flex-col items-center">
            <MapPin className="h-12 w-12 text-blue-600 mb-3" />
            <span className="text-4xl font-bold text-gray-900">0+</span>
            <p className="text-gray-600">Places Discovered</p>
          </div>
          <div className="flex flex-col items-center">
            <img src="/path/to/travelers-icon.svg" alt="Travelers" className="h-12 w-12 mb-3" />
            <span className="text-4xl font-bold text-gray-900">1000+</span>
            <p className="text-gray-600">Happy Travelers</p>
          </div>
          <div className="flex flex-col items-center">
            <img src="/path/to/reviews-icon.svg" alt="Reviews" className="h-12 w-12 mb-3" />
            <span className="text-4xl font-bold text-gray-900">5000+</span>
            <p className="text-gray-600">Reviews & Ratings</p>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gray-800 text-white py-8 text-center">
        <p>&copy; {new Date().getFullYear()} Travel Suite. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default Home;

