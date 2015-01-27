using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models
{
    public class Town
    {
        public string Name { get; set; }
        public DbGeography Location { get; set; }
    }
}
