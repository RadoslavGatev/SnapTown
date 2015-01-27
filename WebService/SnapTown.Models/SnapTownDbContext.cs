using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.Entity;

namespace SnapTown.Models
{
    public class SnapTownDbContext : DbContext
    {
        public SnapTownDbContext()
            : base("DefaultConnection")
        {
        }

        public DbSet<Country> Countries { get; set; }
        public DbSet<Media> Media { get; set; }
        public DbSet<Town> Towns { get; set; }
        public DbSet<User> Users { get; set; }
    }
}
