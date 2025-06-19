import React, { useState, useEffect } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { MapPin, Star, MessageCircle, Edit, Trash2, PlusCircle } from 'lucide-react';

const PlaceDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user, API_BASE_URL } = useAuth();
  const [place, setPlace] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [newComment, setNewComment] = useState('');
  const [newRating, setNewRating] = useState(0);

  useEffect(() => {
    fetchPlace();
  }, [id]);

  const fetchPlace = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/places/${id}`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        setPlace(data);
      } else {
        setError('Place not found');
      }
    } catch (err) {
      setError('Failed to fetch place details');
      console.error('Error fetching place:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this place?')) {
      try {
        const response = await fetch(`${API_BASE_URL}/places/${id}`, {
          method: 'DELETE',
          credentials: 'include'
        });
        if (response.ok) {
          navigate('/places');
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

  const handleAddComment = async (e) => {
    e.preventDefault();
    if (!newComment.trim()) return;

    try {
      const response = await fetch(`${API_BASE_URL}/comments/place/${id}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({ content: newComment })
      });
      if (response.ok) {
        setNewComment('');
        fetchPlace(); // Refresh comments
      } else {
        const errText = await response.text();
        setError(errText || 'Failed to add comment');
      }
    } catch (err) {
      setError('Network error during comment submission');
      console.error('Error adding comment:', err);
    }
  };

  const handleAddRating = async () => {
    if (newRating === 0) return;

    try {
      const response = await fetch(`${API_BASE_URL}/ratings/place/${id}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({ score: newRating })
      });
      if (response.ok) {
        setNewRating(0);
        fetchPlace(); // Refresh ratings
      } else {
        const errText = await response.text();
        setError(errText || 'Failed to add rating');
      }
    } catch (err) {
      setError('Network error during rating submission');
      console.error('Error adding rating:', err);
    }
  };

  if (loading) {
    return <div className="text-center py-12">Loading place details...</div>;
  }

  if (error) {
    return <div className="text-center py-12 text-red-600">Error: {error}</div>;
  }

  if (!place) {
    return <div className="text-center py-12">Place not found.</div>;
  }

  const isOwner = user && place.user && user.id === place.user.id;

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <div className="bg-white shadow-md rounded-lg p-6 mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-4">{place.name}</h1>
        <p className="text-gray-600 mb-4">{place.description}</p>
        <div className="flex items-center text-gray-500 text-sm mb-4">
          <MapPin className="h-4 w-4 mr-2" />
          <span>{place.location}</span>
        </div>
        {place.user && (
          <div className="text-gray-500 text-sm mb-4">
            Posted by: <span className="font-medium">{place.user.username}</span>
          </div>
        )}

        {isOwner && (
          <div className="flex space-x-4 mb-6">
            <Link
              to={`/places/edit/${place.id}`}
              className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              <Edit className="h-4 w-4 mr-2" /> Edit Place
            </Link>
            <button
              onClick={handleDelete}
              className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
            >
              <Trash2 className="h-4 w-4 mr-2" /> Delete Place
            </button>
          </div>
        )}

        {/* Images Section */}
        <h2 className="text-2xl font-semibold text-gray-900 mb-4">Images</h2>
        {place.images && place.images.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mb-6">
            {place.images.map((image) => (
              <div key={image.id} className="relative rounded-lg overflow-hidden shadow-md">
                <img src={`${API_BASE_URL}/images/${image.id}`} alt={image.description || 'Place Image'} className="w-full h-48 object-cover" />
                {image.description && (
                  <div className="absolute bottom-0 left-0 right-0 bg-black bg-opacity-50 text-white text-sm p-2">
                    {image.description}
                  </div>
                )}
              </div>
            ))}
          </div>
        ) : (
          <p className="text-gray-500 mb-6">No images available for this place.</p>
        )}
        {user && user.role === 'GUIDE' && isOwner && (
          <Link
            to={`/places/${place.id}/upload-image`}
            className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
          >
            <PlusCircle className="h-4 w-4 mr-2" /> Upload Image
          </Link>
        )}

        {/* Ratings Section */}
        <h2 className="text-2xl font-semibold text-gray-900 mb-4">Ratings</h2>
        {place.ratings && place.ratings.length > 0 ? (
          <div className="mb-6">
            <div className="flex items-center mb-2">
              <Star className="h-5 w-5 text-yellow-400 mr-2" />
              <span className="text-xl font-bold text-gray-800">
                {(place.ratings.reduce((sum, r) => sum + r.score, 0) / place.ratings.length).toFixed(1)}
              </span>
              <span className="text-gray-500 ml-2">({place.ratings.length} ratings)</span>
            </div>
            <div className="space-y-2">
              {place.ratings.map((rating) => (
                <div key={rating.id} className="flex items-center text-sm text-gray-600">
                  <span className="font-medium">{rating.user ? rating.user.username : 'Anonymous'}</span>:
                  <span className="ml-2 flex items-center">
                    {[...Array(rating.score)].map((_, i) => (
                      <Star key={i} className="h-4 w-4 text-yellow-400 fill-current" />
                    ))
                    }
                    {[...Array(5 - rating.score)].map((_, i) => (
                      <Star key={i} className="h-4 w-4 text-gray-300" />
                    ))
                    }
                  </span>
                </div>
              ))}
            </div>
          </div>
        ) : (
          <p className="text-gray-500 mb-6">No ratings yet.</p>
        )}
        {user && user.role === 'TRAVELER' && (
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-800 mb-2">Rate this place:</h3>
            <div className="flex items-center space-x-2">
              {[1, 2, 3, 4, 5].map((score) => (
                <Star
                  key={score}
                  className={`h-6 w-6 cursor-pointer ${newRating >= score ? 'text-yellow-400 fill-current' : 'text-gray-300'}`}
                  onClick={() => setNewRating(score)}
                />
              ))}
              <button
                onClick={handleAddRating}
                disabled={newRating === 0}
                className="ml-4 px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Submit Rating
              </button>
            </div>
          </div>
        )}

        {/* Comments Section */}
        <h2 className="text-2xl font-semibold text-gray-900 mb-4">Comments</h2>
        {place.comments && place.comments.length > 0 ? (
          <div className="space-y-4 mb-6">
            {place.comments.map((comment) => (
              <div key={comment.id} className="bg-gray-50 p-4 rounded-lg shadow-sm">
                <div className="flex items-center mb-2">
                  <MessageCircle className="h-5 w-5 text-gray-500 mr-2" />
                  <span className="font-medium text-gray-800">{comment.user ? comment.user.username : 'Anonymous'}</span>
                  <span className="text-sm text-gray-500 ml-auto">{new Date(comment.createdAt).toLocaleDateString()}</span>
                </div>
                <p className="text-gray-700">{comment.content}</p>
              </div>
            ))}
          </div>
        ) : (
          <p className="text-gray-500 mb-6">No comments yet.</p>
        )}
        {user && (
          <form onSubmit={handleAddComment} className="mb-6">
            <h3 className="text-lg font-semibold text-gray-800 mb-2">Add a comment:</h3>
            <textarea
              className="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              rows="4"
              placeholder="Write your comment here..."
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              required
            ></textarea>
            <button
              type="submit"
              className="mt-3 px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              Submit Comment
            </button>
          </form>
        )}
      </div>
    </div>
  );
};

export default PlaceDetail;

