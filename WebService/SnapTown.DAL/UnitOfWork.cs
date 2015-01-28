using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SnapTown.Models;

namespace SnapTown.DAL
{
    public class UnitOfWork : IUnitOfWork
    {
        private SnapTownDbContext dbContext;
        public UnitOfWork(SnapTownDbContext dbContext, IRepository<Media> mediaRepo,
            IRepository<Subscription> subscriptionsRepo, IRepository<User> usersRepo,
            IRepository<Town> townsRepo)
        {
            this.dbContext = dbContext;

            this.Media = mediaRepo;
            this.Subscriptions = subscriptionsRepo;
            this.Users = usersRepo;
            this.Towns = townsRepo;
        }

        public IRepository<Media> Media { get; private set; }

        public IRepository<Subscription> Subscriptions { get; private set; }

        public IRepository<User> Users { get; private set; }
        public IRepository<Town> Towns { get; private set; }


        public void Dispose()
        {
            if (this.dbContext != null)
            {
                this.dbContext.Dispose();
            }
        }

        public void SaveChanges()
        {
            this.dbContext.SaveChanges();
        }
    }
}
