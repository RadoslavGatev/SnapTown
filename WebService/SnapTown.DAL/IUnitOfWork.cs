using SnapTown.Models;
using System;

namespace SnapTown.DAL
{
    public interface IUnitOfWork : IDisposable
    {
        IRepository<User> Users { get; }
        IRepository<Media> Media { get; }
        IRepository<Subscription> Subscriptions { get; }
        IRepository<Town> Towns { get; }


        /// <summary>
        /// Saves the changes.
        /// </summary>
        void SaveChanges();
    }
}
