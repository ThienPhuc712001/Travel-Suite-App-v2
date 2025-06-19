import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { MapPin, Edit, Trash2, PlusCircle } from 'lucide-react';

const MyPlaces = () => {
  const { user, API_BASE_URL } = useAuth();
  const [places, setPlaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (user) {
      fetchMyPlaces();
    } else {
      setLoading(false);
      setError('Please log in to view your places.');
    }
  }, [user]);

  const fetchMyPlaces = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/places/my`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        setPlaces(data);
      } else {
        setError('Failed to fetch your places');
      }
    } catch (err) {
      setError('Network error: Could not connect to API');
      console.error('Error fetching my places:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (placeId) => {
    if (window.confirm('Are you sure you want to delete this place?')) {
      try {
        const response = await fetch(`${API_BASE_URL}/places/${placeId}`, {
          method: 'DELETE',
          credentials: 'include'
        });
        if (response.ok) {
          fetchMyPlaces(); // Refresh the list
        } else {
          const errText = await response.text();
          setError(errText || 'Failed to delete place');
        }
      } catch (err) {
        setError('Network error during deletion');
        console.error('Error deleting place:', err);
      }
    }
  };

  if (loading) {
    return <div className="text-center py-12">Loading your places...</div>;
  }

  if (error) {
    return <div className="text-center py-12 text-red-600">Error: {error}</div>;
  }

  if (!user || user.role !== 'GUIDE') {
    return <div className="text-center py-12 text-red-600">You must be logged in as a GUIDE to view this page.</div>;
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">My Places</h1>
        <Link
          to="/places/new"
          className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
        >
          <PlusCircle className="h-5 w-5 mr-2" /> Add New Place
        </Link>
      </div>

      {places.length === 0 ? (
        <div className="text-center py-12">
          <MapPin className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">No places found</h3>
          <p className="mt-1 text-sm text-gray-500">
            You haven't added any places yet. Start by adding one!
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {places.map((place) => (
            <div
              key={place.id}
              className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow"
            >
              <Link to={`/places/${place.id}`}>
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
                  </div>
                </div>
              </Link>
              <div className="p-4 border-t border-gray-200 flex justify-end space-x-2">
                <Link
                  to={`/places/edit/${place.id}`}
                  className="inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-yellow-500 hover:bg-yellow-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-400"
                >
                  <Edit className="h-4 w-4 mr-1" /> Edit
                </Link>
                <button
                  onClick={() => handleDelete(place.id)}
                  className="inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
                >
                  <Trash2 className="h-4 w-4 mr-1" /> Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default MyPlaces;

