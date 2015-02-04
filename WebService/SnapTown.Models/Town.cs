using System;
using System.Collections.Generic;
using System.Data.Entity.Spatial;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SnapTown.Models
{
    public class Town
    {
        public int TownID { get; set; }
        public string Name { get; set; }
        public string OtherSpellings { get; set; }
        public DbGeography Location { get; set; }

        public int CountryID { get; set; }

        public virtual Country Country { get; set; }
        public virtual ICollection<Subscription> Subscriptions { get; set; }
    }
}
